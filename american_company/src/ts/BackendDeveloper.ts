import {Employee} from "./Employee";
import {Project} from "./Project";

class BackendDeveloper implements Employee {
    tool:"Java"|"C#"
    name: string;
    project: Project;

    constructor(name: string, project: Project,tool:"Java"|"C#") {
        this.name = name
        this.project = project
        this.tool = tool
    }

    getCurrentProject(): Project {
        return this.project;
    }
    getName(): string {
        return this.name;
    }
}

export {BackendDeveloper}