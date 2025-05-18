import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Customer } from '../models/customer';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private readonly url = 'http://localhost:8085/customers';

  constructor(private http:HttpClient) {
  }

  public getCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(this.url);
  }
  
}
