import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { accountDetails } from '../models/accountDetails';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private readonly url = 'http://localhost:8085/accounts';

  constructor(private http: HttpClient) { }

  //fetch all accounts
  public getAccounts():Observable<accountDetails[]>{
    return this.http.get<accountDetails[]>(this.url);
  }

  //search a bank account
  public getAccount(id : string, page: number, size: number):Observable<accountDetails>{
    return this.http.get<accountDetails>(`${this.url}/${id}/pageOperations?page=${page}&size=${size}`);
  }

  public debit(accountId: string, amount: number, description: string){
    let payload = {accountId, amount, description};
    return this.http.post(`${this.url}/debit`,payload);
  }

  public credit(accountId: string, amount: number, description: string){
    let payload = {accountId, amount, description};
    return this.http.post(`${this.url}/credit`,payload);
  }

  public transfer(accountSrc: string, accountDst: string, amount: number, description: string){
    let payload = {accountSrc, accountDst, amount, description};
    return this.http.post(`${this.url}/debit`,payload);
  }
}
