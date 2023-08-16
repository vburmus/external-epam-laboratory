import {Project} from "./Project";
import {Employee} from "./Employee";

class Company {

    private employees: Employee[]

    constructor();
    constructor(projects: Project[]);
    constructor(projects: Project[], employees: Employee[]);

    constructor(...args: any[]) {
        if (args.length == 0) {
            this.employees = []
        } else if (args.length == 1) {
            this.employees = args[0] as Employee[]
        } else {
            throw new Error('Invalid constructor arguments');
        }
    }

    public addEmployee(employee: Employee) {
        this.employees.push(employee)
    }

    public getProjectList(): Project[] {
        const projects: Project[] = []
        this.employees.forEach(employee => {
            const project = employee.getCurrentProject();
            const projectNames = projects.map(existingProject => existingProject.getName())
            if (project && !projectNames.includes(project.getName())) {
                projects.push(project)
            }
        });
        return projects;
    }

    public getEmployeeNameList(): string[] {
        return this.employees.map(employee => employee.getName())
    }
}

export {Company}