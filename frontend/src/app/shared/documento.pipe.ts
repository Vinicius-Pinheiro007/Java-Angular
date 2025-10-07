import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'documento', standalone: true })
export class DocumentoPipe implements PipeTransform {
  transform(value: string | number | null | undefined): string {
    if (value === null || value === undefined) return '';
    const digits = String(value).replace(/\D+/g, '');
    if (digits.length === 11) {
      return digits
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d{2})$/, '$1-$2');
    }
    if (digits.length === 14) {
      return digits
        .replace(/(\d{2})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d)/, '$1/$2')
        .replace(/(\d{4})(\d{2})$/, '$1-$2');
    }
    return String(value);
  }
}


