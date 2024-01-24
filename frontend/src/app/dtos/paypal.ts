export interface PaypalLinkDto {
  href: string;
  rel: string;
  method: string;
}

export interface PaypalDonationDto {
  amount: string;
  donator: string;
  receiver: string;
}
