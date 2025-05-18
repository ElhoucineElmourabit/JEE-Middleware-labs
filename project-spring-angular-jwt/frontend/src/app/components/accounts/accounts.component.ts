import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountService } from '../../shared/services/account.service';
import { accountDetails } from '../../shared/models/accountDetails';
import { catchError, Observable, throwError } from 'rxjs';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-accounts',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './accounts.component.html',
  styleUrl: './accounts.component.css'
})
export class AccountsComponent {

   constructor(private accountService: AccountService){
  }

  customers$: Observable<accountDetails[]> | undefined;
  errorMessage: string | null = null;
  
  searchForm = new FormGroup({
    accountId: new FormControl(''),
  });

  currentPage: number = 0;
  pageSize: number = 5;
  accountObservable : Observable<accountDetails> | undefined;

  handleSearch(){
    let accountId: string = this.searchForm.value.accountId ?? '';
    this.accountObservable = this.accountService.getAccount(accountId, this.currentPage, this.pageSize);
  }

  gotoPage(page: number) {
    this.currentPage=page;
    this.handleSearch();
  }

  operationFromGroup = new FormGroup({
    operationType: new FormControl(''),
    amount: new FormControl(''),
    description: new FormControl(''),
    accountDestination: new FormControl('')
  });

  handleAccountOperation(){
    let accountId: string = this.searchForm.value.accountId ?? '';
    let operationType = this.operationFromGroup.value.operationType;
    let amount = Number(this.operationFromGroup.value.amount ?? 0);
    let description = this.operationFromGroup.value.description ?? '';
    let accountDestination = this.operationFromGroup.value.accountDestination ?? '';

    if(operationType == 'DEBIT'){
        this.accountService.debit(accountId, amount, description).subscribe({
          next: () => {
            alert("Debit succeeded");
          },
          error: () => {
            console.log('debit failed');
          }
        });
    }else if(operationType == 'CREDIT'){
        this.accountService.credit(accountId, amount, description).subscribe({
          next: () => {
            alert('credit succeeded');
          },
          error: () => {
            alert('credit failed');
          }
        });
    }else if(operationType == 'TRANSFER') {
        this.accountService.transfer(accountId, accountDestination, amount, description).subscribe({
          next: () => {
            alert('transfer succeeded');
          },
          error: () => {
            alert('transfer failed');
          }
        });
    }
  }
}
