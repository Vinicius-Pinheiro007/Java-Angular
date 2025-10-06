import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'filterBy', standalone: true })
export class FilterByPipe implements PipeTransform {
  transform<T>(items: T[], term: string | undefined | null, prop: keyof T): T[] {
    if (!items) return [];
    if (!term) return items;
    const t = (term || '').toString().toLowerCase();
    return items.filter((it: any) => (it?.[prop] || '').toString().toLowerCase().includes(t));
  }
}



