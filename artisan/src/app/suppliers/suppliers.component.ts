import { Component } from '@angular/core';
import { AddSupplierComponent } from './add-supplier/add-supplier.component';
import { EditSupplierComponent } from './edit-supplier/edit-supplier.component';
import { CommonModule } from '@angular/common';
import { SuppliersService } from '../services/suppliers.service';

@Component({
  selector: 'app-suppliers',
  standalone: true,
  imports: [AddSupplierComponent, EditSupplierComponent, CommonModule],
  templateUrl: './suppliers.component.html',
  styleUrl: './suppliers.component.css'
})
export class SuppliersComponent {
  showAddSupplier: boolean = false;
  showEditSupplier: boolean = false;
  suppliers!: any[];
  selectedSupplier: any;

  constructor(private suppliersService: SuppliersService) { }

  ngOnInit(): void {
    this.getSuppliers();
  }

  toggleAddSupplier(): void {
    this.showAddSupplier = !this.showAddSupplier;
  }

  toggleEditSupplier(): void {
    this.showEditSupplier = !this.showEditSupplier;
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

  handleSupplierAdded(response: any) {
    this.suppliers = response;
  }

  handleSupplierEdited(response: any) {
    this.suppliers = response;
  }
  editSupplier(supplier: any) {
    // Pass the selected supplier to the edit supplier component
    this.selectedSupplier = supplier;
    // Toggle the visibility of the edit supplier component
    this.toggleEditSupplier();
  }
  deleteSupplier(supplier: any) {
    const { id } = supplier;
    this.suppliersService.deleteSupplier(id).subscribe({
      next: (response) => {
        this.suppliers = response;
      },
      error: (error) => {
        if (error.error.error) {
          alert(error.error.error)
        } else {
          alert('unknown error occured')
        }
      }
    });
  }
}
