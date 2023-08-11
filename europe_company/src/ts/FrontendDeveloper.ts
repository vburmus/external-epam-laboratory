import {Employee} from "./Employee";
import {Project} from "./Project";

class FrontendDeveloper extends Employee {
    constructor(name: string, project: Project) {
        super(name, project);
    }
}

export {FrontendDeveloper}