import { Component } from '@angular/core';
import { SitesService } from '../services/sites.service';
import { CreditsService } from '../services/credits.service';
import { AddCreditComponent } from './add-credit/add-credit.component';
import { CommonModule } from '@angular/common';
import { DetailsComponent } from './details/details.component';
import { SuppliersService } from '../services/suppliers.service';
import { AccountsService } from '../services/accounts.service';
import { CashService } from '../services/cash.service';

@Component({
  selector: 'app-credits',
  standalone: true,
  imports: [AddCreditComponent, CommonModule, DetailsComponent],
  templateUrl: './credits.component.html',
  styleUrl: './credits.component.css'
})
export class CreditsComponent {
  showAddCredit: boolean = false;
  showDetails: boolean = false;
  sites: Array<any> = []
  suppliers: Array<any> = []
  credits: any;
  selectedCredit: any;
  accounts!: any[];
  types!: string[];

  constructor(private cashService: CashService, private creditsService: CreditsService, private accountsService: AccountsService, private sitesService: SitesService, private suppliersService: SuppliersService) { }


  ngOnInit(): void {
    this.getSites();
    this.getCredits();
    this.getSuppliers();
    this.getAccounts();
    this.getTypes()
  }
  getSites(): void {
    this.sitesService.getSites().subscribe({
      next: (response) => {
        this.sites = response;
      },
      error: (error) => {
        console.error('Error fetching sites:', error);
      }
    });
  }

  getTypes(): void {
    this.cashService.getTypes().subscribe({
      next: (response) => {
        this.types = response;
      },
      error: (error) => {
        console.error('Error fetching types:', error);
      }
    });
  }

  getAccounts(): void {
    this.accountsService.getAccounts().subscribe({
      next: (response) => {
        this.accounts = response;
      },
      error: (error) => {
        console.error('Error fetching accounts:', error);
      }
    });
  }
  getSuppliers(): void {
    this.suppliersService.getSuppliers().subscribe({
      next: (response) => {
        this.suppliers = response;
      },
      error: (error) => {
        console.error('Error fetching suppliers:', error);
      }
    });
  }
  getCredits(): void {
    this.creditsService.getCredits().subscribe({
      next: (response) => {
        this.credits = response;
      },
      error: (error) => {
        console.error('Error fetching sites:', error);
      }
    });
  }

  toggleAddCredit() {
    this.showAddCredit = !this.showAddCredit
  }
  toggleDetails() {
    this.showDetails = !this.showDetails
  }

  handleCreditAdded(response: any) {
    this.credits = response;
  }

  viewDetails(credit: any) {
    // Pass the selected site to the edit site component
    this.selectedCredit = credit;
    // Toggle the visibility of the edit site component
    this.toggleDetails();
  }

}
