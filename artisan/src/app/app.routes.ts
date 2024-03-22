import { Routes } from '@angular/router';
import { SitesComponent } from './sites/sites.component';
import { CreditsComponent } from './credits/credits.component';
import { SuppliersComponent } from './suppliers/suppliers.component';

export const routes: Routes = [
    { path: '', redirectTo: '/sites', pathMatch: 'full' },
    { path: 'sites', component: SitesComponent },
    { path: 'credits', component: CreditsComponent },
    { path: 'suppliers', component: SuppliersComponent },
];

