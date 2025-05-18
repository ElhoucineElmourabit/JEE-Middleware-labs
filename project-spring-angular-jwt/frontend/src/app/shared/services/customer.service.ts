import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Customer } from '../models/customer';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private readonly url = 'http://localhost:8085/customers';

  constructor(private http: HttpClient) {}

  //fetch customers
  public getCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(this.url);
  }

  //search customers
  public searchCustomers(keyword: string): Observable<Customer[]> {
    return this.http.get<Customer[]>(`${this.url}/search?keyword=${keyword}`);
  }

  //add a customer
  public addCustomer(customer: Customer): Observable<Customer> {
    return this.http.post<Customer>(this.url, customer);
  }

  //delete a customer
  public deleteCustomer(id: number | undefined) {
    return this.http.delete(`${this.url}/${id}`);
  }

  
}
