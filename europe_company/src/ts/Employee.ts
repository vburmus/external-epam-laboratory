import {Project} from "./Project";

class Employee {
    protected name: string;
    protected project: Project;

    constructor(name: string, project: Project) {
        this.name = name;
        this.project = project;
    }

    public getName(): string {
        return this.name;
    }

    public getCurrentProject(): Project {
        return this.project;
    }

    public setName(name: string): void {
        this.name = name;
    }

    public setCurrentProject(project: Project): void {
        this.project = project;
    }
}

export {Employee}