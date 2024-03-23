import { Component } from '@angular/core';
import { AddCashComponent } from './add-cash/add-cash.component';
import { DetailsComponent } from './details/details.component';
import { CommonModule } from '@angular/common';
import { CashService } from '../services/cash.service';
import { SitesService } from '../services/sites.service';
import { SuppliersService } from '../services/suppliers.service';

@Component({
  selector: 'app-cash',
  standalone: true,
  imports: [AddCashComponent, CommonModule, DetailsComponent],
  templateUrl: './cash.component.html',
  styleUrl: './cash.component.css'
})
export class CashComponent {
  showAddCash: boolean = false;
  showDetails: boolean = false;
  sites: Array<any> = []
  suppliers: Array<any> = []
  cash: any;
  selectedCash: any;
  constructor(private cashService: CashService, private sitesService: SitesService, private suppliersService: SuppliersService) { }


  ngOnInit(): void {
    this.getSites();
    this.getCash();
    this.getSuppliers();
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
  getCash(): void {
    this.cashService.getCash().subscribe({
      next: (response) => {
        this.cash = response;
      },
      error: (error) => {
        console.error('Error fetching sites:', error);
      }
    });
  }

  toggleAddCash() {
    this.showAddCash = !this.showAddCash
  }
  toggleDetails() {
    this.showDetails = !this.showDetails
  }

  handleCashAdded(response: any) {
    this.cash = response;
  }

  viewDetails(cash: any) {
    // Pass the selected site to the edit site component
    this.selectedCash = cash;
    // Toggle the visibility of the edit site component
    this.toggleDetails();
  }

}
