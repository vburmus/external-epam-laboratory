import {Project} from "./Project";
import {Company} from "./Company";
import {FrontendDeveloper} from "./FrontendDeveloper";
import {BackendDeveloper} from "./BackendDeveloper";

function main() {
    let projects: Project[] = [new Project("Book store"), new Project("Car service")]
    let company: Company = new Company(projects)

    company.addEmployee(new FrontendDeveloper("John", projects[0],"Angular"))
    company.addEmployee(new BackendDeveloper("Markus", projects[0],"Java"))
    company.addEmployee(new FrontendDeveloper("Nicolas", projects[1],"React"))
    company.addEmployee(new BackendDeveloper("Adrian", projects[1],"C#"))

    console.log("Company projects:", company.getProjectList().map(a => a.toString()))
    console.log("Company employees:", company.getEmployeeNameList())
}
main()