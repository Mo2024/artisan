import { Routes } from '@angular/router';
import { SitesComponent } from './sites/sites.component';
import { CreditsComponent } from './credits/credits.component';
import { SuppliersComponent } from './suppliers/suppliers.component';
import { CashComponent } from './cash/cash.component';
import { AccountsComponent } from './accounts/accounts.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';

export const routes: Routes = [
    { path: '', redirectTo: '/sites', pathMatch: 'full' },
    { path: 'sites', component: SitesComponent },
    { path: 'auth/login', component: LoginComponent },
    { path: 'auth/register', component: RegisterComponent },
    { path: 'purchases/credits', component: CreditsComponent },
    { path: 'purchases/cash', component: CashComponent },
    { path: 'suppliers', component: SuppliersComponent },
    { path: 'accounts', component: AccountsComponent },
];

