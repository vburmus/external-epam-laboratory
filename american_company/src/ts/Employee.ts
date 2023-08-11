import {Project} from "./Project";

interface Employee {
    name: string;
    project: Project;

    getName(): string
    getCurrentProject(): Project
}

export {Employee}