import { Component, EventEmitter, Input, Output } from '@angular/core';
import { SitesService } from '../../services/sites.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-edit-site',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-site.component.html',
  styleUrl: './edit-site.component.css'
})
export class EditSiteComponent {
  @Output() closeClicked: EventEmitter<void> = new EventEmitter<void>();
  @Output() siteEdited: EventEmitter<any> = new EventEmitter();
  @Input() site: any;

  name: string = ''
  description: string = ''


  constructor(private sitesService: SitesService) { }
  ngOnInit(): void {
    this.name = this.site.name
    this.description = this.site.description
  }
  editSite(id: number, name: string, description: string) {
    if (!name.trim() || !description.trim()) {
      alert('Name and description cannot be empty');
      return; // Exit the function if empty
    }

    this.sitesService.editSite(id, name, description).subscribe({
      next: (response) => {
        console.log('Site edited:', response);
        this.siteEdited.emit(response); // Emit the edited site to parent component
        this.closeClicked.emit(); // Close the edit site component
      },
      error: (error) => {
        console.error('Error editing site:', error);
      }
    });
  }

  emitCloseClicked(): void {
    this.closeClicked.emit();
  }
}
