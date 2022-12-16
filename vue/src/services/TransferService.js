import axios from 'axios';

export default {

  getAllTransfers() {
    return axios.get('')
  },

  // also works for pending depending on the transfer type id
  sendBucks(transfer) {
    return axios.post('/transfer', transfer)
  },

  getAllTransfersByUserId(id) {
    return axios.get(`/transfer/user/${id}`)
  },

  getAllPendingTransfersByUserId(id) {
      return axios.get(`/transfer/pending/user/${id}`)
  },

  getTransferByTransferId(id) {
      return axios.get(`transfer/${id}`)
  },

  updateTransferStatusByTransferId(id) {
      return axios.put(`/transfer/${id}`)
  }

}
