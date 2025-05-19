import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../shared/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  standalone: true,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  constructor(private authService: AuthService, private router: Router) {}

  loginForm = new FormGroup({
    username: new FormControl(''),
    password: new FormControl(''),
  });

  handleLogin() {
    let username = this.loginForm.value.username ?? '';
    let pwd = this.loginForm.value.password ?? '';
    this.authService.login(username, pwd).subscribe({
      next: (data) => {
        console.log("Login success:", data);
      this.authService.loadProfile(data);
      console.log("Navigating to /admin...");
      this.router.navigateByUrl("/admin");
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
}
