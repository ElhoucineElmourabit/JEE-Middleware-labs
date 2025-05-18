import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomerService } from '../../shared/services/customer.service';
import { Customer } from '../../shared/models/customer';
import { catchError, map, Observable, of, throwError } from 'rxjs';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';


@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css'
})
export class CustomersComponent implements OnInit {

  constructor(private customerService: CustomerService, private router: Router){
  }

  customers$: Observable<Customer[]> | undefined;
  errorMessage: string | null = null;
  
  
  ngOnInit(): void {
    this.customers$ = this.customerService.getCustomers().pipe(
      catchError(err => {
        this.errorMessage = err?.message;
        return throwError(() => err);
      })
    );
  }

  searchForm = new FormGroup({
    keyword: new FormControl(''),
  });

  handleSearch(){
    const kw = this.searchForm.value.keyword ?? '';
    this.customers$ = this.customerService.searchCustomers(kw).pipe(
      catchError(err => {
        this.errorMessage = err?.message;
        return throwError(() => err);
      })
    ); 
    this.searchForm.reset();
  }

  handleDelete(customer: Customer){
    let c = confirm("You sure ?");
    if(!c){
      return;
    }
    this.customerService.deleteCustomer(customer.id).subscribe({
      next: data => {
          this.handleSearch();
        },
      error: err => {
        console.log(err);
      }
    }
    );
  }

  handleAccounts(customer: Customer){
    console.log('click');
    this.router.navigateByUrl(`/customer-accounts/${customer.id}`)
  }
}
