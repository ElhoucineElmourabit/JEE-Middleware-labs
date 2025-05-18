import { Component } from '@angular/core';
import { CustomerService } from '../../shared/services/customer.service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Customer } from '../../shared/models/customer';
import { Router } from '@angular/router';

@Component({
  selector: 'app-new-customer',
  imports: [ReactiveFormsModule],
  standalone: true,
  templateUrl: './new-customer.component.html',
  styleUrl: './new-customer.component.css'
})
export class NewCustomerComponent {
 

  constructor(private customerService:CustomerService, private router: Router){}

  newCustomerForm = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(4)]),
    email: new FormControl('', [Validators.required, Validators.email]),
  });

  handleSubmit(){
   let name = this.newCustomerForm.value.name ?? '';
   let email = this.newCustomerForm.value.email ?? '';
   let customer: Customer ={name, email}
   this.customerService.addCustomer(customer).subscribe({
    next: data => {
      alert("Customer saved !");
      //this.newCustomerForm.reset();
      this.router.navigateByUrl("/customers");
    },
    error: err => {
      console.log(err);
    }
   });
  }
}
