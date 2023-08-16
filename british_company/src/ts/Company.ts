import {Project} from "./Project.js";
import {Employee} from "./Employee.js";
import {ILocation} from "./ILocation.js";

export class Company<L extends ILocation> {
    private location: L | null

    constructor();
    constructor(location: L);
    constructor(location: L, projects: Project[]);
    constructor(...args: any[]) {
        if (args.length == 0) {
            this.location = null
        } else if (args.length == 1) {
            this.location = args[0] as L
        } else {
            throw new Error('Invalid constructor arguments');
        }
    }

    public addEmployee(employee: Employee): void {
        if(this.location) {
            this.location.addPerson(employee);
        }
    }

    public getProjectList(): Project[] {
        const projects: Project[] = []
        this.location?.employees.forEach(employee => {
            const project = employee.getCurrentProject();
            const projectNames = projects.map(existingProject => existingProject.getName())
            if (project && !projectNames.includes(project.getName())) {
                projects.push(project)
            }
        });
        return projects;
    }

    public getEmployeeNameList(): string[] {
        let names: string[] = []
        this.location?.employees.forEach(employee => names.push(employee.getName()))
        return names
    }
}