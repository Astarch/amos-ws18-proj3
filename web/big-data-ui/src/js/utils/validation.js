const emailRegex = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
const smallCharRegex = /[a-z]+/;
const capitalCharRegex = /[A-Z]+/;
const numberCharRegex = /[0-9]+/;
const specialCharRegex = /[^A-Za-z0-9]+/;


// calculates the strength of a password and returns the strength on a scale from
// 0 to 8 where 8 is a very strong password and 0 a very weak one
const getPasswordStrength = (password) => {
  let strength = 0;

  if (password.length > 0) {
    strength += smallCharRegex.test(password); // contains a-z
    strength += capitalCharRegex.test(password); // contains A-Z
    strength += numberCharRegex.test(password); // contains 0-9
    strength += specialCharRegex.test(password); // contains special character
    strength += Math.floor(Math.min(4, password.length / 3)); // 0-12 characters result in 0-4 points
  }
  return strength;
}

const isMailValid = (mail) => mail.length > 0 && emailRegex.test(mail);
const isPasswordValid = (password) => password.length > 4;
const isNameValid = (name) => name.length > 2;


export {
  isMailValid,
  isPasswordValid,
  isNameValid,
  getPasswordStrength
}

