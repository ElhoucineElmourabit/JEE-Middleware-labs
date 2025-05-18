import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Customer } from '../../shared/models/customer';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../../shared/services/customer.service';

@Component({
  selector: 'app-customer-account',
  imports: [CommonModule],
  templateUrl: './customer-account.component.html',
  styleUrl: './customer-account.component.css'
})
export class CustomerAccountComponent {
  customerId! : string ;
  customer! : Customer;
  constructor(private route : ActivatedRoute, private router :Router, private customerService: CustomerService) {
    this.customer=this.router.getCurrentNavigation()?.extras.state as Customer;
  }

  ngOnInit(): void {
    this.customerId = this.route.snapshot.params['id'];

    
  }
}
