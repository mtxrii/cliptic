'use client';

import { useEffect } from "react";

const WEEKDAYS = [
  "sunday",
  "monday",
  "tuesday",
  "wednesday",
  "thursday",
  "friday",
  "saturday",
];

export default function DayThemeCycle() {
  useEffect(() => {
    const root = document.documentElement;

    const applyThemeForToday = () => {
      const today = WEEKDAYS[new Date().getDay()];
      root.setAttribute("data-weekday", today);
    };

    applyThemeForToday();
    const intervalId = window.setInterval(applyThemeForToday, 60 * 1000);

    return () => {
      window.clearInterval(intervalId);
    };
  }, []);

  return null;
}
