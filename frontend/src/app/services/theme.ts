import { Injectable, signal } from '@angular/core';

export type ThemeMode = 'light' | 'dark';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private readonly storageKey = 'sq-theme';
  theme = signal<ThemeMode>(this.getInitialTheme());

  constructor() {
    this.applyTheme(this.theme());
  }

  toggleTheme(): void {
    const nextTheme: ThemeMode = this.theme() === 'light' ? 'dark' : 'light';
    this.theme.set(nextTheme);
    this.applyTheme(nextTheme);
    try {
      localStorage.setItem(this.storageKey, nextTheme);
    } catch {
      // Ignore if localStorage unavailable
    }
  }

  private getInitialTheme(): ThemeMode {
    try {
      const saved = localStorage.getItem(this.storageKey) as ThemeMode;
      if (saved === 'light' || saved === 'dark') {
        return saved;
      }
    } catch {
      // Ignore
    }
    return 'light'; // Default to pristine light canvas
  }

  private applyTheme(mode: ThemeMode): void {
    if (typeof document !== 'undefined') {
      document.documentElement.setAttribute('data-theme', mode);
    }
  }
}
