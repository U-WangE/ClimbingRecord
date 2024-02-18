package com.ihavesookchi.climbingrecord.data.dataState
 sealed class UpdateDataState {
     object UpdateSuccess : UpdateDataState()
     object UpdateFailure : UpdateDataState()
     object AttemptLimitExceeded : UpdateDataState()
}