import {Project} from "./Project.js";
import {Employee} from "./Employee.js";
import {ILocation} from "./ILocation.js";

export class Company<L extends ILocation> {
    protected projects: Project[]
    private location: L | null

    constructor();
    constructor(location: L);
    constructor(location: L, projects: Project[]);
    constructor(...args: any[]) {
        if (args.length == 0) {
            this.location = null
            this.projects = []
        } else if (args.length == 1) {
            this.location = args[0] as L
            this.projects = []
        } else if (args.length == 2) {
            this.location = args[0] as L
            this.projects = args[1] as Project[]
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
        return this.projects
    }

    public getEmployeeNameList(): string[] {
        let names: string[] = []
        this.location?.employees.forEach(employee => names.push(employee.getName()))
        return names
    }
}