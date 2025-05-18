import { Routes } from '@angular/router';
import {CustomersComponent} from './components/customers/customers.component';
import {AccountsComponent} from './components/accounts/accounts.component';
import { NewCustomerComponent } from './components/new-customer/new-customer.component';

export const routes: Routes = [
  { path: "customers",
    title: "Customers",
    component: CustomersComponent
  },
  { path: "accounts",
    title: "Accounts",
    component: AccountsComponent
  },
  { path: "new-customer",
    title: "New Customer",
    component: NewCustomerComponent
  },
];
