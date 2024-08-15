import { Routes } from '@angular/router';
import { SitesComponent } from './sites/sites.component';
import { CreditsComponent } from './credits/credits.component';
import { SuppliersComponent } from './suppliers/suppliers.component';
import { CashComponent } from './cash/cash.component';
import { AccountsComponent } from './accounts/accounts.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { authGuard } from './auth.guard';

export const routes: Routes = [
    { path: '', redirectTo: '/sites', pathMatch: 'full' },
    { path: 'sites', component: SitesComponent, canActivate: [authGuard] },
    { path: 'auth/login', component: LoginComponent },
    { path: 'auth/register', component: RegisterComponent },
    { path: 'purchases/credits', component: CreditsComponent, canActivate: [authGuard] },
    { path: 'purchases/cash', component: CashComponent, canActivate: [authGuard] },
    { path: 'suppliers', component: SuppliersComponent, canActivate: [authGuard] },
    { path: 'accounts', component: AccountsComponent, canActivate: [authGuard] },
];

