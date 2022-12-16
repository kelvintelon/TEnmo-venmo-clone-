import axios from 'axios';

export default {

  getBalance() {
    return axios.get('/account/balance')
  },

  getAllAccounts() {
    return axios.get('/account')
  },

  // needed to send/request bucks
  getAccountIdByUserId(id) {
  return axios.get(`/account/user/${id}`)
  }

  

}
