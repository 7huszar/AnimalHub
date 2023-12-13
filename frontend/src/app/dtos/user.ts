
export interface UserDetailDto {
  id: string,
  email: string,
  role: string,
}

export interface UserSearch {
  email?: string,
  role?:string,
}
