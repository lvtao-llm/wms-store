import Cookies from 'js-cookie'

const state = {
  customDiaShow: Cookies.get('customShow') || false
}

const mutations = {
  SET_Flag: (state, type) => {
    state.customDiaShow = type
    Cookies.set('customShow', type)
  },

}

const actions = {

}

export default {
  state,
  mutations,
  actions
}
