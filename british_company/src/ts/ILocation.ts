import {Employee} from "./Employee.js";

export interface ILocation{
    employees: Employee[]
    city:string
    addPerson(person:Employee):void;
    getPerson(index:number):Employee;
    getCount():number;
    getEmployees():Employee[]
}
