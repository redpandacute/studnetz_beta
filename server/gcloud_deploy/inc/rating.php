<?php

class RATING
{
/** Bayesian approach according to https://stackoverflow.com/questions/1411199/what-is-a-better-way-to-sort-by-a-5-star-rating
 *
 * requires an array with all the possible ratings and their weights: e.g. 1 point = 1, 2 point = 3, 3 points = 10 points etc.
 *
 * requires counted ratings 
 */

	public static function bayesianRating($weighting, $ratings, $z)
	{
		$byrating = RATING::bayesianSum($weighting, $ratings) - $z * ((RATING::bayesianSumSQ($weighting, $ratings) - RATING::bayesianSum($weighting, $ratings)^2) / (RATING::sum($ratings) + count(weighting) + 1))^0.5;
		
		return $byrating;
	}

	
	
	public static function bayesianSum($weighting, $ratings) {
		
		$k = count($pointWeighting);
		$n = RATING::sum($ratings);
		$bysum = 0;

		for($i = 0; $i < $k; $i++)
		{
			$bysum += $pointWeighting[$i] * ($ratings[$i] + 1) / ($k + $n);
		}

		return $bysum;
	}

	public static function bayesianSumSQ($weighting, $ratings) 
	{
		
		$k = count($pointWeighting);
		$n = RATING::sum($ratings);
		$bysum = 0;

		for($i = 0; $i < $k; $i++)
		{
			$bysum += $pointWeighting[$i]^2 * ($ratings[$i] + 1) / ($k + $n);
		}

		return $bysum;
	}

/** sorting algorithm (insertion sort)
 *	recommended when n < 16, which is not really good for our case
 */

	public static function insSort($dataset)
	{
		$cnt = count($dataset);

		for($i = 1; $i < $cnt; $i++)
		{
			$tmp = $dataset[$i - 1];

			for($n = $i; $n < $cnt; $n++)
			{
				if($tmp < $dataset[$n])
				{
					$dataset[$i - 1] = $dataset[$n];
					$dataset[$n] = $tmp;
					$tmp = $dataset[$i - 1];
				}
			}
		}

		return $dataset;
	}

/** merge sort
 *
 */

	public static function mergeSort($dataset)
	{
		if(count($dataset) == 1)
		{
			return $dataset;
		}	
		
		$q = floor(count($dataset)/2);

		$L = array_slice($dataset, 0, $q);
		$R = array_slice($dataset, $q);

		$L = RATING::mergeSort($L);
		$R = RATING::mergeSort($R);
		$dataset = RATING::merge($L, $R);
		return $dataset;
	}

	private static function merge($L, $R)
	{
		$out = array();
		
		$iL = 0;
		$iR = 0;

		while($iL < count($L) && $iR < count($R))
		{
			if($R[$iR] > $L[$iL])
			{
				$out[] = $R[$iR];
				$iR ++;
			}
			else
			{
				$out[] = $L[$iL];
				$iL ++;
			}	
		}

		while($iL < count($L))
		{
			$out[] = $L[$iL];
			$iL ++;
		}

		while($iR < count($R))
		{
			$out[] = $R[$iR];
			$iR ++;
		}
		return $out;
	}

/**
 * Rating system based on the IMDB ratingsystem according to sources on sites:
 * 
 * https://stackoverflow.com/questions/6017208/adding-an-extra-factor-number-of-clicks-to-a-bayesian-ranking-system?noredirect=1&lq=1
 * 
 * https://stackoverflow.com/questions/1411199/what-is-a-better-way-to-sort-by-a-5-star-rating
 *
 * FUNCTION::
 *
 * rating =: weighted rating
 * r =: avg rating
 * v =: number of ratings
 * c =: avg rating over all data
 * m =: smoothing
 *
 * rating = (v / (v+m)) * r + (m / (v+m)) * c;
 *
 * simplified:
 *
 * rating = (R*v+m*C)/(v+m)
 *
 *
 * NOTE: this is not a very good way of sorting for out problem since it just adds imaginary ratings based on the average in order to give all the results an equal chance, not exaclty what we want.
 */

	public static function IMDBRating($r, $v, $c, $m)
	{
		$rating = ($r*$v + $m*$c)/($v + $m);
		return rating;
	}	


/**
 * Calculates the average rating of an input array
 */	
	public static function average($dataset)
	{
		$avg = 0;

		$avg = sum($dataset)/count($dataset);
		
		return $avg;
	}


/** calculating the sum of an array*/

	public static function sum($dataset)
	{
		foreach($dataset as $data)
		{
			$sum += $data;
		}
		
		return $sum;
	}

}
?>
