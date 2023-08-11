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
            this.projects = args[0] as Project[]
            this.employees = []
        } else if (args.length == 2) {
            this.projects = args[0] as Project[]
            this.employees = args[1] as Employee[]
        } else {
            throw new Error('Invalid constructor arguments');
        }
    }

    public addEmployee(employee: Employee) {
        this.employees.push(employee)
    }

    public getProjectList(): Project[] {
        return this.projects
    }

    public getEmployeeNameList(): string[] {
        let names: string[] = []
        this.employees.forEach(employee => names.push(employee.getName()))
        return names
    }
}

export {Company}