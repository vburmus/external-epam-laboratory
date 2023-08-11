import {ILocation} from "./ILocation.js";
import {Employee} from "./Employee.js";

export class CompanyLocationLocalStorage implements ILocation{
    private static readonly STORAGE_KEY = "employees";

    employees: Employee[];
    city:string
    constructor(city:string) {
       // const storedEmployees = localStorage.getItem(CompanyLocationLocalStorage.STORAGE_KEY);
      //  this.employees = storedEmployees ? JSON.parse(storedEmployees) : [];
        this.employees = []
        this.city = city
    }
    addPerson(employee: Employee): void {
        this.employees.push(employee)
    }

    getCount(): number {
        return this.employees.length;
    }

    getPerson(index: number): Employee {
        if(index >= this.employees.length){
            throw new Error("ArrayIndexOutOfBound")
        }
        return this.employees[index];
    }
    saveEmployees(): void {
        localStorage.setItem(CompanyLocationLocalStorage.STORAGE_KEY, JSON.stringify(this.employees));
    }
    getEmployees(): Employee[] {
        return this.employees;
    }
}
