import { Component } from '@angular/core';
import { NavbarComponent } from '../navbar/navbar.component';
import { RouterModule, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-admin-template',
  imports: [NavbarComponent, RouterModule, RouterOutlet],
  templateUrl: './admin-template.component.html',
  styleUrl: './admin-template.component.css',
  standalone: true
})
export class AdminTemplateComponent {

}
