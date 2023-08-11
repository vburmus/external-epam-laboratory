import {Employee} from "./Employee";
import {Project} from "./Project";

class FrontendDeveloper implements Employee {
    tool:"React"|"Angular"
    name: string;
    project: Project;

    constructor(name: string, project: Project,tool:"React"|"Angular") {
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

export {FrontendDeveloper}