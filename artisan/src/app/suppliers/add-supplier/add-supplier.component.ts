import { Component, EventEmitter, Output } from '@angular/core';
import { SuppliersService } from '../../services/suppliers.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-supplier',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-supplier.component.html',
  styleUrl: './add-supplier.component.css'
})
export class AddSupplierComponent {
  @Output() closeClicked: EventEmitter<void> = new EventEmitter<void>();
  @Output() supplierAdded: EventEmitter<any> = new EventEmitter();

  name: string = '';
  description: string = '';
  isDisabled: boolean = false;

  constructor(private suppliersService: SuppliersService) { }

  addSupplier(name: string, description: string) {
    if (!name.trim() || !description.trim()) {
      alert('Name and description cannot be empty');
      return; // Exit the function if empty
    }

    this.isDisabled = true;
    this.suppliersService.addSupplier(name, description).subscribe({
      next: (response) => {
        this.isDisabled = false;
        console.log('Suypplier added:', response);
        this.supplierAdded.emit(response);
        this.closeClicked.emit();
      },
      error: (error) => {
        this.isDisabled = false
        if (error.error.error) {
          alert(error.error.error)
        } else {
          alert('unknown error occured')
        }
      }
    });
  }

  emitCloseClicked(): void {
    this.closeClicked.emit();
  }
}
