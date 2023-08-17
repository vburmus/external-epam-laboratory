import {Project} from "./Project";
import {Employee} from "./Employee";

class Company {
    private employees: Employee[]
    private projects: Project[]

    constructor();
    constructor(projects: Project[]);
    constructor(projects: Project[], employees: Employee[]);

    constructor(...args: any[]) {
        if (args.length == 0) {
            this.employees = []
            this.projects = []
        } else if (args.length == 1) {
            this.employees = args[0] as Employee[]
            this.projects = []
        } else if (args.length == 2) {
            this.employees = args[0] as Employee[]
            this.projects = args[1] as Project[]
        } else {
            throw new Error('Invalid constructor arguments');
        }
    }

    public addEmployee(employee: Employee) {
        this.employees.push(employee)
        const employeeProject = employee.getCurrentProject()
        if (!this.projects.includes(employeeProject)) this.projects.push(employeeProject)
    }

    public getProjectList(): Project[] {
        return this.projects
    }

    public getEmployeeNameList(): string[] {
        return this.employees.map(employee => employee.getName())
    }
}

export {Company}