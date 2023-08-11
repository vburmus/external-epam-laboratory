import {Project} from "./Project.js";
import {Company} from "./Company.js";
import {Location} from "./Location.js";
import {CompanyLocationArray} from "./CompanyLocationArray.js";
import {CompanyLocationLocalStorage} from "./CompanyLocationLocalStorage.js";
import {Employee} from "./Employee.js";


function main() {
    let projects: Project[] = [new Project("Book store"), new Project("Car service")]

    const europeLocation = new Location("Europe");
    const britishLocation = new CompanyLocationArray("UK");
    const americaLocation = new CompanyLocationLocalStorage("USA");

    let companyEurope: Company<Location>;
    companyEurope = new Company<Location>(europeLocation,projects);
    const companyBritish = new Company<CompanyLocationArray>(britishLocation,projects);
   //const companyAmerica = new Company<CompanyLocationLocalStorage>(americaLocation);

    const employee1 = new Employee("John",projects[0]);
    const employee2 = new Employee("Jane",projects[0]);
    const employee3 = new Employee("Michael",projects[0]);

    companyEurope.addEmployee(employee1);
    companyBritish.addEmployee(employee2);
    //companyAmerica.addEmployee(employee3);

    const employee4 = new Employee("John",projects[1]);
    const employee5 = new Employee("Jane",projects[1]);
    const employee6 = new Employee("Michael",projects[1]);

    companyEurope.addEmployee(employee4);
    companyBritish.addEmployee(employee5);
    //companyAmerica.addEmployee(employee6);
    console.log("Employees:", companyEurope.getEmployeeNameList(), " , projects: ", companyEurope.getProjectList())
    console.log("Employees:", companyBritish.getEmployeeNameList(), " , projects: ", companyBritish.getProjectList())
}
main()