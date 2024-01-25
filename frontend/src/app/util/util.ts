// Removes all occurrences of element in array
export function removeElement<T>(array: T[], element: T): T[] {
  return array.filter((e, i) => e !== element);
}
