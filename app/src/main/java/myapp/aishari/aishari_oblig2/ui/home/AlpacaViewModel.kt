package myapp.aishari.aishari_oblig2.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import myapp.aishari.aishari_oblig2.data.model.AlpacaVote
import myapp.aishari.aishari_oblig2.domain.DataSourceImp
import retrofit2.HttpException
import java.io.IOException

class AlpacaViewModel : ViewModel() {
    private val _parties = MutableStateFlow<AlpacaUiState>(AlpacaUiState.Idle)
    val parties: StateFlow<AlpacaUiState> = _parties.asStateFlow()
    private val _votesD3 = MutableStateFlow<AlpacaUiState>(AlpacaUiState.Idle)
    val votesD3: StateFlow<AlpacaUiState> = _votesD3.asStateFlow()

    private val _totalVotes = MutableStateFlow<List<AlpacaVote>?>(null)
    val totalVotes = _totalVotes.asStateFlow()
    private var partiesCount = 0


    init {
        getParties()
        getVotes(1)

    }

    fun getParties() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _parties.emit(AlpacaUiState.Loading)
                DataSourceImp().getParties().collect {
                    if (it != null) {
                        _parties.emit(AlpacaUiState.Successful(it))
                        partiesCount = it.size
                    } else
                        _parties.emit(AlpacaUiState.Error("Error Happened"))

                }
            } catch (e: IOException) {
                e.printStackTrace()
                _parties.emit(AlpacaUiState.Error(e.message.toString()))

            } catch (e: HttpException) {
                e.printStackTrace()
                _parties.emit(AlpacaUiState.Error(e.message.toString()))
            } catch (e: Exception) {
                e.printStackTrace()
                _parties.emit(AlpacaUiState.Error(e.message.toString()))
            }
        }
    }

    fun getVotes(path: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _votesD3.emit(AlpacaUiState.Loading)
                if (path == 3) {
                    DataSourceImp().getVotesD3().collect {
                        if (it != null) {
                            _votesD3.emit(AlpacaUiState.Successful(it))
                            val arrayList: ArrayList<AlpacaVote> = ArrayList()
                            for (item in it) {
                                var total = 0
                                for (i in it) {
                                    total += i.votes
                                }
                                Log.e("TAG", "getVotes: $total")
                                arrayList.add(
                                    AlpacaVote(
                                        item.id,
                                        item.votes,
                                        (100 * item.votes / total)
                                    )
                                )

                            }

                            _totalVotes.emit(arrayList)
                        } else
                            _votesD3.emit(AlpacaUiState.Error("Error Happened"))

                    }
                } else {

                    DataSourceImp().getVotes(path).collect {
                        if (it != null) {
                            _votesD3.emit(AlpacaUiState.Successful(it))
                            val arrayList: ArrayList<AlpacaVote> = ArrayList()
                            for (i in 1..partiesCount) {
                                val votes = it.filter { it.id == "$i" }.size
                                arrayList.add(AlpacaVote(i, votes, (100 * votes / it.size)))

                            }
                            _totalVotes.emit(arrayList)

                        } else
                            _votesD3.emit(AlpacaUiState.Error("Error Happened"))

                    }

                }

            } catch (e: IOException) {
                e.printStackTrace()
                _votesD3.emit(AlpacaUiState.Error(e.message.toString()))

            } catch (e: HttpException) {
                e.printStackTrace()
                _votesD3.emit(AlpacaUiState.Error(e.message.toString()))
            } catch (e: Exception) {
                e.printStackTrace()
                _votesD3.emit(AlpacaUiState.Error(e.message.toString()))
            }
        }
    }
}