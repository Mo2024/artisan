import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SuppliersService } from '../../services/suppliers.service';

@Component({
  selector: 'app-edit-supplier',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-supplier.component.html',
  styleUrl: './edit-supplier.component.css'
})
export class EditSupplierComponent {
  @Output() closeClicked: EventEmitter<void> = new EventEmitter<void>();
  @Output() supplierEdited: EventEmitter<any> = new EventEmitter();
  @Input() supplier: any;

  name: string = ''
  description: string = ''


  constructor(private suppliersService: SuppliersService) { }
  ngOnInit(): void {
    this.name = this.supplier.name
    this.description = this.supplier.description
  }
  editSupplier(id: number, name: string, description: string) {
    if (!name.trim() || !description.trim()) {
      alert('Name and description cannot be empty');
      return; // Exit the function if empty
    }

    this.suppliersService.editSupplier(id, name, description).subscribe({
      next: (response) => {
        // console.log('Supplier edited:', response);
        this.supplierEdited.emit(response); // Emit the edited supplier to parent component
        this.closeClicked.emit(); // Close the edit supplier component
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

  emitCloseClicked(): void {
    this.closeClicked.emit();
  }
}
