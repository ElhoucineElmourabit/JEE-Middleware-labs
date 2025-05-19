import { Routes } from '@angular/router';
import { CustomersComponent } from './components/customers/customers.component';
import { AccountsComponent } from './components/accounts/accounts.component';
import { NewCustomerComponent } from './components/new-customer/new-customer.component';
import { CustomerAccountComponent } from './components/customer-account/customer-account.component';
import { LoginComponent } from './components/login/login.component';
import { AdminTemplateComponent } from './components/admin-template/admin-template.component';
import { authenticationGuard } from './shared/guards/authentication.guard';
import { authorizationGuard } from './shared/guards/authorization.guard';
import { NotAuthorizedComponent } from './components/not-authorized/not-authorized.component';

export const routes: Routes = [
  { path: 'login', title: 'Login', component: LoginComponent },
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full',
  },
  {
    path: 'admin',
    title: 'Admin',
    component: AdminTemplateComponent,
    canActivate: [authenticationGuard],
    children: [
      { path: 'customers', title: 'Customers', component: CustomersComponent },
      { path: 'accounts', title: 'Accounts', component: AccountsComponent },
      {
        path: 'new-customer',
        title: 'New Customer',
        component: NewCustomerComponent,
        canActivate: [authorizationGuard], data: {role:"ADMIN"}
      },
      {
        path: 'customer-accounts/:id',
        title: 'Customer Account',
        component: CustomerAccountComponent,
      },
      {
        path: 'notAuthorized',
        title: 'Not Authorized',
        component: NotAuthorizedComponent,
      }
    ],
  },
];
