from datetime import datetime
import joblib
import pandas as pd


class SleepAnalysisService:

    def __init__(self):
        self.model = self.load_model('/Users/the9kim/VitaMetrics/data_analysis/vitametric_analysis/sleep_prediction_model.pkl')

    @staticmethod
    def load_model(model_path):
        try:
            return joblib.load(model_path)
        except FileNotFoundError:
            raise Exception(f"Model file not found at path: {model_path}")

    def predict_sleep_duration(self, date: str) -> str:
        date_obj = pd.to_datetime(date, errors='coerce')
        if pd.isnull(date_obj):
            raise ValueError("Invalid Date format. Use 'YYYY-MM-DD'.")

        input_data = self._prepare_input(date_obj)
        predicted_time = self.model.predict(input_data)
        return self._format_prediction(predicted_time)

    def _prepare_input(self, date: datetime) -> pd.DataFrame:

        sleep_data = pd.DataFrame({'creationDate': [date]})
        sleep_data['creationDate'] = pd.to_datetime(sleep_data['creationDate'])

        sleep_data = self._add_time_features(sleep_data)
        sleep_data.set_index('creationDate', inplace=True)

        return sleep_data

    @staticmethod
    def _add_time_features(sleep_data: pd.DataFrame) -> pd.DataFrame:
        sleep_data['dayofweek'] = sleep_data.creationDate.dt.dayofweek
        sleep_data['quater'] = sleep_data.creationDate.dt.quarter
        sleep_data['year'] = sleep_data.creationDate.dt.year
        sleep_data['dayofyear'] = sleep_data.creationDate.dt.dayofyear
        sleep_data['dayofmonth'] = sleep_data.creationDate.dt.day
        sleep_data['weekofyear'] = sleep_data.creationDate.dt.isocalendar().week

        return sleep_data

    @staticmethod
    def _format_prediction(predicted_minutes: float) -> str:
        hours = int(predicted_minutes // 60)
        minutes = int(predicted_minutes % 60)

        return f"The expected sleep duration is {hours} hours and {minutes} minutes"

if __name__ == '__main__':

    service = SleepAnalysisService()
    prediction = service.predict_sleep_duration('2024.11.17')
    print(prediction)




