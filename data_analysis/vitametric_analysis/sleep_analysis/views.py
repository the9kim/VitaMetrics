import json

from django.http import JsonResponse
from rest_framework.decorators import api_view

from .service import SleepAnalysisService


@api_view(['POST'])
def predict_sleep_duration(request):
    try:
        data = json.loads(request.body)
        date_str = data.get('date')

        if not date_str:
            return JsonResponse({'error': 'Date not provided in request'}, status=400)

        service = SleepAnalysisService()
        prediction = service.predict_sleep_duration(date_str)

        if prediction is None:
            return JsonResponse({'error': 'No sleep records found'}, status=404)

        return JsonResponse({'hours': prediction['hours'], 'minutes': prediction['minutes']})


    except ValueError:
        return JsonResponse({'error': 'Invalid date format. Expected format: YYYY-MM-DD'}, status=400)

    except Exception as e:
        return JsonResponse({'error': str(e)}, status=500)
