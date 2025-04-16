class LocationService {
    constructor() {
        this.currentPosition = null;
        this.currentAddress = null;
    }

    /**
     * Get the current location from the device
     * @returns {Promise} A promise that resolves with the location data
     */
    getCurrentLocation() {
        return new Promise((resolve, reject) => {
            if (!navigator.geolocation) {
                reject(new Error('Geolocation is not supported by your browser'));
                return;
            }

            navigator.geolocation.getCurrentPosition(
                (position) => {
                    const location = {
                        latitude: position.coords.latitude,
                        longitude: position.coords.longitude
                    };
                    resolve(location);
                },
                (error) => {
                    reject(error);
                },
                {
                    enableHighAccuracy: true,
                    timeout: 5000,
                    maximumAge: 0
                }
            );
        });
    }

    /**
     * Convert an address to coordinates
     * @param {string} address The address to geocode
     * @returns {Promise} A promise that resolves with the geocoding result
     */
    async geocodeAddress(address) {
        const response = await fetch(`/api/location/geocode?address=${encodeURIComponent(address)}`);
        const data = await response.json();
        
        if (data.error) {
            throw new Error(data.error);
        }
        
        return {
            latitude: data.latitude,
            longitude: data.longitude,
            formattedAddress: data.formattedAddress
        };
    }

    /**
     * Get the last known location
     * @returns {Object|null} The last known location or null if none exists
     */
    getLastLocation() {
        return this.currentPosition ? {
            ...this.currentPosition,
            address: this.currentAddress
        } : null;
    }
}

// Create a global instance of the LocationService
window.locationService = new LocationService();

// Function to update emergency form with current location
async function updateEmergencyFormWithLocation() {
    try {
        const location = await getCurrentLocation();
        console.log('Current location:', location);
        
        // Update the form fields with the current location
        document.getElementById('latitude').value = location.latitude;
        document.getElementById('longitude').value = location.longitude;
        
        // Get the address using reverse geocoding
        const response = await fetch(`/api/location/reverse-geocode?latitude=${location.latitude}&longitude=${location.longitude}`);
        if (!response.ok) {
            throw new Error('Failed to get address');
        }
        
        const data = await response.json();
        console.log('Reverse geocoding response:', data);
        
        if (data.address) {
            document.getElementById('pickupLocation').value = data.address;
        } else {
            document.getElementById('pickupLocation').value = `${location.latitude}, ${location.longitude}`;
        }
    } catch (error) {
        console.error('Error getting location:', error);
        alert('Unable to get your current location. Please enter it manually.');
    }
}

// Add event listener to the "Get Current Location" button
document.addEventListener('DOMContentLoaded', () => {
    const locationButton = document.getElementById('getCurrentLocation');
    if (locationButton) {
        locationButton.addEventListener('click', updateEmergencyFormWithLocation);
    }
}); 