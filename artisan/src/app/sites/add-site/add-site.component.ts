import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { SitesService } from '../../services/sites.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-site',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-site.component.html',
  styleUrl: './add-site.component.css'
})
export class AddSiteComponent {
  @Output() closeClicked: EventEmitter<void> = new EventEmitter<void>();
  @Output() siteAdded: EventEmitter<any> = new EventEmitter();

  name: string = '';
  description: string = '';

  constructor(private sitesService: SitesService) { }

  addSite(name: string, description: string) {
    if (!name.trim() || !description.trim()) {
      alert('Name and description cannot be empty');
      return; // Exit the function if empty
    }

    this.sitesService.addSite(name, description).subscribe({
      next: (response) => {
        console.log('Site added:', response);
        this.siteAdded.emit(response);
        this.closeClicked.emit();
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
