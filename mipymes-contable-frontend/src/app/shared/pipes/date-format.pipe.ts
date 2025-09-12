// src/app/shared/pipes/date-format.pipe.ts - Pipe para formato de fecha
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'appDateFormat'
})
export class DateFormatPipe implements PipeTransform {
  transform(value: string | Date | null | undefined, format: 'short' | 'medium' | 'long' = 'medium'): string {
    if (!value) {
      return '-';
    }

    const date = typeof value === 'string' ? new Date(value) : value;

    if (isNaN(date.getTime())) {
      return '-';
    }

    const options: Intl.DateTimeFormatOptions = {
      timeZone: 'America/Guatemala'
    };

    switch (format) {
      case 'short':
        options.day = '2-digit';
        options.month = '2-digit';
        options.year = 'numeric';
        break;
      case 'medium':
        options.day = '2-digit';
        options.month = 'short';
        options.year = 'numeric';
        break;
      case 'long':
        options.weekday = 'long';
        options.day = '2-digit';
        options.month = 'long';
        options.year = 'numeric';
        break;
    }

    return date.toLocaleDateString('es-GT', options);
  }
}
