import Vue from 'vue'
import {getAllKmConfig} from '@/api/api'
import {KM_ALL_CONFIG, SITE_INFO, USER_INFO} from '@/store/mutation-types'
import siteLogo from '@assets/logo.png'
import siteBannerLogo from '@assets/km_logo.png'
import { getFileAccessHttpUrl } from '@api/manage'

const site = {
  state: {
    siteInfo: {
      siteTitle: '科亿知识库',
      siteHomePageUrl: 'http://www.kykms.cn',
      siteCopyRight: '科亿信息技术',
      siteAvatar: siteLogo,
      siteTitleBanner: siteBannerLogo
    },
    kmConfig: {
      DefaultPageUseTopicList: '0',
      HeaderBackgroundColor: '#001529'
    }
  },

  mutations: {
    SET_KMCONFIG: (state, config) => {
      state.kmConfig = config
      Vue.prototype.kmConfig =  config
      Vue.ls.set(KM_ALL_CONFIG, config, 7 * 24 * 60 * 60 * 1000)
    },
    SET_SITE_INFO: (state, siteInfo) => {
      state.siteInfo.siteTitle = siteInfo.siteTitle !== undefined &&  siteInfo.siteTitle !== null? siteInfo.siteTitle : state.siteInfo.siteTitle
      state.siteInfo.siteHomePageUrl = siteInfo.siteHomePageUrl !== undefined && siteInfo.siteHomePageUrl !== null ? siteInfo.siteHomePageUrl : state.siteInfo.siteHomePageUrl
      state.siteInfo.siteCopyRight = siteInfo.siteCopyRight !== undefined && siteInfo.siteCopyRight !== null? siteInfo.siteCopyRight : state.siteInfo.siteCopyRight
      state.siteInfo.siteAvatar = siteInfo.siteAvatar !== '' &&  siteInfo.siteAvatar !== undefined && siteInfo.siteAvatar !== null? getFileAccessHttpUrl(siteInfo.siteAvatar) : state.siteInfo.siteAvatar
      state.siteInfo.siteTitleBanner = siteInfo.siteTitleBanner !== '' && siteInfo.siteTitleBanner !== undefined && siteInfo.siteTitleBanner !== null? getFileAccessHttpUrl(siteInfo.siteTitleBanner) : state.siteInfo.siteTitleBanner
      Vue.ls.set(SITE_INFO, state.siteInfo, 7 * 24 * 60 * 60 * 1000)
    }
  },

  actions: {
    GetSiteInfo({ commit }) {
      return new Promise((resolve, reject) => {
        getAllKmConfig().then(response => {
          let siteInfo = {}
          console.log("----getSiteInfo--------",response);
          if(response.success){
            let result = response.result
            console.log('result:',result)
            commit('SET_KMCONFIG', result)
            if(result !== null && result !== undefined ){
              siteInfo.siteTitle = result['SiteTitle']
              siteInfo.siteHomePageUrl = result['SiteHomePageUrl']
              siteInfo.siteCopyRight = result['SiteCopyRight']
              siteInfo.siteAvatar = result['SiteAvatar']
              siteInfo.siteTitleBanner = result['SiteTitleBanner']
            }
            console.log("----siteInfo--------",siteInfo)
            commit('SET_SITE_INFO', siteInfo)
            // Vue.ls.set(SITE_INFO, siteInfo, 7 * 24 * 60 * 60 * 1000)
            resolve(response)
          }else{
            resolve(response)
          }
        }).catch(error => {
          reject(error)
        })
      })
    }

  }
}

export default site