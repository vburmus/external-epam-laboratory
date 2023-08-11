import {Project} from "./Project";
import {Employee} from "./Employee";

class BackendDeveloper extends Employee {
    constructor(name: string, project: Project) {
        super(name, project);
    }
}

export {BackendDeveloper}