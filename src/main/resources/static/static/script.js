document.addEventListener('DOMContentLoaded', function() {
    // Funcionalidad del formulario
    const formulario = document.getElementById('formularioViaticos');
    const resultado = document.getElementById('resultado');

    // Funcionalidad del modo oscuro
    const themeSwitch = document.getElementById('themeSwitch');
    const body = document.body;

    // Verificar si hay una preferencia guardada
    const darkMode = localStorage.getItem('darkMode');

    // Verificar preferencia del sistema
    const prefiereDarkMode = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;

    // Aplicar modo oscuro si está guardado o si el usuario lo prefiere
    if (darkMode === 'true' || (darkMode === null && prefiereDarkMode)) {
        body.classList.add('dark-mode');
        themeSwitch.checked = true;
    }

    // Cambiar tema cuando se activa el switch
    themeSwitch.addEventListener('change', function() {
        if (this.checked) {
            body.classList.add('dark-mode');
            localStorage.setItem('darkMode', 'true');
        } else {
            body.classList.remove('dark-mode');
            localStorage.setItem('darkMode', 'false');
        }
    });

    // Escuchar cambios en la preferencia del sistema
    if (window.matchMedia) {
        window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', e => {
            const newDarkMode = e.matches;
            if (newDarkMode) {
                body.classList.add('dark-mode');
                themeSwitch.checked = true;
            } else {
                body.classList.remove('dark-mode');
                themeSwitch.checked = false;
            }
            localStorage.setItem('darkMode', newDarkMode ? 'true' : 'false');
        });
    }

    // Procesamiento del formulario
    formulario.addEventListener('submit', function(event) {
        event.preventDefault();

        // Validar campos
        const nombre = document.getElementById('nombre').value.trim();
        const monto = document.getElementById('monto').value;
        const localidad = document.getElementById('localidad').value.trim();
        const fecha = document.getElementById('fecha').value;

        if (!nombre || !monto || !localidad || !fecha) {
            resultado.className = 'error';
            resultado.textContent = 'Por favor, complete todos los campos.';
            resultado.style.display = 'block';
            return;
        }

        // Mostrar mensaje de procesamiento
        resultado.className = 'success';
        resultado.textContent = 'Generando PDF, espere un momento...';
        resultado.style.display = 'block';

        // Construir URL para descargar el PDF
        const params = new URLSearchParams();
        params.append('nombre', nombre);
        params.append('monto', monto);
        params.append('localidad', localidad);
        params.append('fecha', fecha);

        // Redirigir para descargar el PDF
        window.location.href = '/generar-pdf?' + params.toString();

        // Mostrar mensaje de éxito después de un momento
        setTimeout(function() {
            resultado.textContent = 'Se ha generado el PDF con el formulario de viáticos.';
        }, 2000);
    });
});