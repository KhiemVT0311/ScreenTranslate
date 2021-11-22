package com.eup.screentranslate.database.history;

import androidx.lifecycle.LiveData;

import com.eup.screentranslate.model.History;

import java.util.List;

public class HistoryRepository {
    public HistoryDao historyDao;
    public LiveData<List<History>> listTransHistory;
    public LiveData<List<History>> listTextHistory;
    public LiveData<List<History>> listObjectHistory;

    public HistoryRepository(HistoryDao historyDao) {
        this.historyDao = historyDao;
        this.listTransHistory = historyDao.getHistoryByType(History.TRANS);
        this.listTextHistory = historyDao.getHistoryByType(History.TEXT);
        this.listObjectHistory = historyDao.getHistoryByType(History.OBJECT);
    }

    public void insertOrUpdate(String textTrans, String resultTrans, String orgCode, String destCode){
        History history = checkExist(textTrans);
        if (history == null ){
            historyDao.insert(new History(
                    0,
                    "",
                    System.currentTimeMillis(),
                    textTrans,
                    resultTrans,
                    History.TRANS,
                    orgCode,
                    destCode
            ));
        } else {
            history.date = System.currentTimeMillis();
            historyDao.update(history);
        }
    }

    public void deleteSingle(History history){
        if (history != null)
            historyDao.deleteSingle(history);
    }

    public History checkExist(String textTrans){
        return historyDao.checkExist(textTrans);
    }
}
