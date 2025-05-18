import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomerService } from '../../shared/services/customer.service';
import { Customer } from '../../shared/models/customer';
import { catchError, Observable, of, throwError } from 'rxjs';

@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css'
})
export class CustomersComponent implements OnInit {

  constructor(private customerService: CustomerService){
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
}
